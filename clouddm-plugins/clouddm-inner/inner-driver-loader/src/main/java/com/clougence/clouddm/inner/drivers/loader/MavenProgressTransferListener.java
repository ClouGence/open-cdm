package com.clougence.clouddm.inner.drivers.loader;

import org.eclipse.aether.transfer.AbstractTransferListener;
import org.eclipse.aether.transfer.TransferEvent;
import org.eclipse.aether.transfer.TransferResource;

import com.clougence.drivers.DriverPrepareProgress;
import com.clougence.drivers.DriverVersion;
import com.clougence.drivers.def.ResDef;

final class MavenProgressTransferListener extends AbstractTransferListener {

    private final DriverVersion         driverVersion;
    private final ResDef                driverResource;
    private final DriverPrepareProgress progress;

    MavenProgressTransferListener(DriverVersion driverVersion, ResDef driverResource, DriverPrepareProgress progress){
        this.driverVersion = driverVersion;
        this.driverResource = driverResource;
        this.progress = progress;
    }

    @Override
    public void transferProgressed(TransferEvent event) {
        this.progress.onProgress(this.driverVersion, this.driverResource, //
                resolveFileName(event.getResource()), event.getTransferredBytes(), event.getResource().getContentLength());
    }

    private String resolveFileName(TransferResource transferResource) {
        if (transferResource == null || transferResource.getResourceName() == null) {
            return null;
        }

        String resourceName = transferResource.getResourceName().trim();
        int slashIndex = resourceName.lastIndexOf('/');
        return slashIndex >= 0 ? resourceName.substring(slashIndex + 1) : resourceName;
    }
}
