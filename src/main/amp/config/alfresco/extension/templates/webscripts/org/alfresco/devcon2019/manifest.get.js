node = utils.getNodeFromString("workspace://SpacesStore/" + url.templateArgs["uuid"])
model.content = bulkExporter.exportManifestToString(node);