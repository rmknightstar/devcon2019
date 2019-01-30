package org.alfresco.accelerator.bulkexporter.jscript;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.alfresco.accelerator.bulkexporter.BulkExporter;
import org.alfresco.accelerator.bulkexporter.BulkExporter.BulkExportEmitter;
import org.alfresco.accelerator.bulkexporter.emitter.FullJsonEmitter;
import org.alfresco.accelerator.bulkexporter.emitter.ManifestEmitter;
import org.alfresco.repo.jscript.BaseScopableProcessorExtension;
import org.alfresco.repo.jscript.ScriptNode;
import org.alfresco.service.ServiceRegistry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BulkExporterScriptApi extends BaseScopableProcessorExtension {
	private ServiceRegistry serviceRegistry;
	private BulkExporter bulkExporter;
	
	
    public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

	public void setBulkExporter(BulkExporter bulkExporter) {
		this.bulkExporter = bulkExporter;
	}

	Log logger = LogFactory.getLog(BulkExporterScriptApi.class);
	
	public 	void exportFullToFile(ScriptNode topDir, String manifest) {
		try {
			Writer writer = new FileWriter(manifest);
			BulkExportEmitter emitter = new FullJsonEmitter();
			bulkExporter.export(topDir.getNodeRef(), writer, emitter);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Error processing Node/File: " + topDir.getNodeRef().toString() + "/" + manifest,e);
		}		
	}
	public void exportManifestToFile(ScriptNode topDir, String manifest) {
		try {
			Writer writer = new FileWriter(manifest);
			BulkExportEmitter emitter = new ManifestEmitter();
			bulkExporter.export(topDir.getNodeRef(), writer, emitter);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Error processing Node/File: " + topDir.getNodeRef().toString() + "/" + manifest,e);
		}		
	}
	public String exportFullToString(ScriptNode topDir) {
		try {
			Writer writer = new StringWriter();
			BulkExportEmitter emitter = new FullJsonEmitter();
			bulkExporter.export(topDir.getNodeRef(), writer, emitter);
			writer.close();
			return writer.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Error processing Node: " + topDir.getNodeRef().toString(),e);
		}
		return "";
	}
	public String exportManifestToString(ScriptNode topDir) {
		try {
			Writer writer = new StringWriter();
			BulkExportEmitter emitter = new ManifestEmitter();
			bulkExporter.export(topDir.getNodeRef(), writer, emitter);
			writer.close();
			return writer.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Error processing Node: " + topDir.getNodeRef().toString(),e);
		}
		return "";
	}

}
