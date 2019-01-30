package org.alfresco.accelerator.bulkexporter.emitter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
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

public class ManifestEmitter implements BulkExportEmitter {
    Log logger = LogFactory.getLog(FullJsonEmitter.class);
    ServiceRegistry serviceRegistry;
    NodeService nodeService;
    NodeRef topNode;
    Map<NodeRef,List<NodeRef>> paths = new HashMap<NodeRef,List<NodeRef>>();

    @Override
	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
		this.nodeService = this.serviceRegistry.getNodeService();
	}

    @Override
    public void setTopNode(NodeRef topNode) {
    	this.topNode = topNode;
    }
   
	@Override
	public String separatorString() {
		return ",";
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
			return "\n" + jobj.toString(2);			
		} catch (JSONException e) {
			logger.error("Error processing Node: " + nodeRef.toString(),e);
			return "\n" + jobj.toString();			
		}
	}

	@Override
	public boolean includeTopNode() {
		return false;
	}

	@Override
	public String startString() {
		return "[";
	}

	@Override
	public String endString() {
		return "\n]\n";
	}

}
