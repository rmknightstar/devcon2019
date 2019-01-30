package org.alfresco.accelerator.bulkexporter;

import java.io.Writer;

import org.alfresco.accelerator.bulkexporter.utils.TreeWalker;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.NodeRef;



public interface BulkExporter {
	public interface BulkExportEmitter {
		void setServiceRegistry(ServiceRegistry serviceRegistry);
		void setTopNode(NodeRef nodeRef);
		String separatorString();
		String nodeToString(NodeRef nodeRef);
		boolean includeTopNode();
		String startString();
		String endString();
	}
	void export(NodeRef topDir, Writer manifest,BulkExportEmitter emitter);
}
