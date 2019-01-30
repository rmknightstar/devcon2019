package org.alfresco.accelerator.bulkexporter.emitter;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import org.alfresco.accelerator.bulkexporter.BulkExporter.BulkExportEmitter;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.QName;
import org.alfresco.service.namespace.RegexQNamePattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FullJsonEmitter implements BulkExportEmitter {
    Log logger = LogFactory.getLog(FullJsonEmitter.class);
    ServiceRegistry serviceRegistry;
    NodeService nodeService;

    @Override
	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
		this.nodeService = this.serviceRegistry.getNodeService();
	}
    
    @Override
    public void setTopNode(NodeRef topNode) {
    	;
    }
   
	@Override
	public String separatorString() {
		return "";
	}

	@Override
	public String nodeToString(NodeRef nodeRef) {
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
	public boolean includeTopNode() {
		return true;
	}

	@Override
	public String startString() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String endString() {
		// TODO Auto-generated method stub
		return "";
	}

}
