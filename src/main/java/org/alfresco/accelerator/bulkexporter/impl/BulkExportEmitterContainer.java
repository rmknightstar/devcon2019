package org.alfresco.accelerator.bulkexporter.impl;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.Map;
import java.util.Set;

import org.alfresco.accelerator.bulkexporter.BulkExporter.BulkExportEmitter;
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

public class BulkExportEmitterContainer implements TreeWalker.CallbackE  {
    Log logger = LogFactory.getLog(FileFolderTreeBulkExporter.class);
	FileFolderTreeWalker fileFolderTreeWalker;
	ServiceRegistry serviceRegistry;
	NodeService nodeService;
	ContentService contentService;
	Writer metadata;
	BulkExportEmitter emitter;
	boolean firstOne=true;
	
	boolean includeTop;	

	public void setFileFolderTreeWalker(FileFolderTreeWalker fileFolderTreeWalker) {
		this.fileFolderTreeWalker = fileFolderTreeWalker;
	}

	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
		this.nodeService = this.serviceRegistry.getNodeService();
	}

	BulkExportEmitterContainer(Writer metadata,BulkExportEmitter emitter) {
		this.metadata = metadata;
		this.emitter = emitter;
	}
	
	

	@Override
	public void doWork(NodeRef nodeRef) {
		try {
			if (!firstOne) {
				metadata.write(emitter.separatorString());
			} else {
				firstOne = false;
			}
			metadata.write(emitter.nodeToString(nodeRef));
		} catch (Exception e) {
			logger.error("Error processing Node: " + nodeRef.toString(),e);
		}
		
	}

	@Override
	public void startWalk(NodeRef contextNode) {
		emitter.setServiceRegistry(serviceRegistry);
		try {
			metadata.write(emitter.startString());
		} catch (IOException e) {
			logger.error("Error with start string",e);
		}
		if (emitter.includeTopNode()) {
			try {
				metadata.write(emitter.nodeToString(contextNode));
			} catch (IOException e) {
				logger.error("Error processing Node: " + contextNode.toString(),e);
			}
			firstOne=false;
		}
	}

	@Override
	public void endWalk() {
		try {
			metadata.write(emitter.endString());
		} catch (IOException e) {
			logger.error("Error with start string",e);
		}
	}


	public void setIncludeTop(boolean includeTop) {
		this.includeTop = includeTop;
	}
		
}

