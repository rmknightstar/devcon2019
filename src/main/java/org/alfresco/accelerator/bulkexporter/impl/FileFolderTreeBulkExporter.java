package org.alfresco.accelerator.bulkexporter.impl;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.alfresco.accelerator.bulkexporter.BulkExporter;
import org.alfresco.accelerator.bulkexporter.utils.TreeWalker;
import org.alfresco.accelerator.bulkexporter.utils.impl.FileFolderTreeWalker;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.QName;
import org.alfresco.service.namespace.RegexQNamePattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FileFolderTreeBulkExporter implements BulkExporter {
    Log logger = LogFactory.getLog(FileFolderTreeBulkExporter.class);
	FileFolderTreeWalker fileFolderTreeWalker;
	ServiceRegistry serviceRegistry;
	NodeService nodeService;
	ContentService contentService;
	

	public void setFileFolderTreeWalker(FileFolderTreeWalker fileFolderTreeWalker) {
		this.fileFolderTreeWalker = fileFolderTreeWalker;
	}

	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
		this.nodeService = this.serviceRegistry.getNodeService();
	}

	private class BulkExportJsonEmitter implements TreeWalker.Callback  {
		Writer metadata;
		
		BulkExportJsonEmitter(Writer metadata) {
			this.metadata = metadata;
		}
		
		String nodeToJsonLine(NodeRef nodeRef) {
			JSONObject jobj = new JSONObject();
			try {
				jobj.put("nodeRef", nodeRef.toString());
				jobj.put("type", nodeService.getType(nodeRef));
				Set<QName> aspectSet = nodeService.getAspects(nodeRef);
				JSONArray aspectArray = new JSONArray(aspectSet);
				jobj.put("aspects",aspectArray);
				Map<QName,Serializable> props = nodeService.getProperties(nodeRef);
				jobj.put("properties", props);
				jobj.put("path", nodeService.getPath(nodeRef));
				jobj.put("childAssocs", nodeService.getChildAssocs(nodeRef));
				jobj.put("parentAssocs", nodeService.getParentAssocs(nodeRef));
				jobj.put("targetAssocs", nodeService.getTargetAssocs(nodeRef, RegexQNamePattern.MATCH_ALL));
				jobj.put("sourceAssocs", nodeService.getSourceAssocs(nodeRef, RegexQNamePattern.MATCH_ALL));
				
			} catch (JSONException e) {
				logger.error("Error processing Node: " + nodeRef.toString(),e);
			}
			return jobj.toString() + "\n";
		}

		@Override
		public void doWork(NodeRef nodeRef) {
			try {
				metadata.write(nodeToJsonLine(nodeRef));
			} catch (IOException e) {
				logger.error("Error processing Node: " + nodeRef.toString(),e);
			}
			
		}

		@Override
		public void startWalk(NodeRef contextNode) {
			
			doWork(contextNode);
		}

		
	}

	@Override
	public void export(NodeRef topDir, Writer manifest, BulkExportEmitter emitter) {
		BulkExportEmitterContainer beec = new BulkExportEmitterContainer(manifest,emitter);
		beec.setServiceRegistry(serviceRegistry);
		beec.setFileFolderTreeWalker(fileFolderTreeWalker);
		fileFolderTreeWalker.walk(topDir, beec);
	}


}
