/**
 * Copyright (C) 2016 eBusiness Information
 *
 * This file is part of OSM Contributor.
 *
 * OSM Contributor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OSM Contributor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OSM Contributor.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.jawg.osmcontributor.sync;

import com.squareup.okhttp.OkHttpClient;

import org.greenrobot.eventbus.EventBus;
import org.simpleframework.xml.core.Persister;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.jawg.osmcontributor.database.PoiAssetLoader;
import io.jawg.osmcontributor.database.dao.PoiNodeRefDao;
import io.jawg.osmcontributor.database.preferences.LoginPreferences;
import io.jawg.osmcontributor.rest.Backend;
import io.jawg.osmcontributor.rest.OSMProxy;
import io.jawg.osmcontributor.rest.OsmBackend;
import io.jawg.osmcontributor.rest.clients.OsmRestClient;
import io.jawg.osmcontributor.rest.clients.OverpassRestClient;
import io.jawg.osmcontributor.rest.managers.OSMSyncNoteManager;
import io.jawg.osmcontributor.rest.managers.OSMSyncWayManager;
import io.jawg.osmcontributor.rest.managers.SyncNoteManager;
import io.jawg.osmcontributor.rest.managers.SyncWayManager;
import io.jawg.osmcontributor.rest.mappers.NoteMapper;
import io.jawg.osmcontributor.rest.mappers.PoiMapper;
import io.jawg.osmcontributor.rest.utils.XMLMapper;
import io.jawg.osmcontributor.ui.activities.AuthorisationInterceptor;
import io.jawg.osmcontributor.ui.managers.PoiManager;
import io.jawg.osmcontributor.utils.ConfigManager;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.client.OkClient;



@Module
@Singleton
public class SyncModule {

    @Provides
    Backend getBackend(LoginPreferences loginPreferences, EventBus bus, OSMProxy osmProxy, OverpassRestClient overpassRestClient, OsmRestClient osmRestClient, PoiMapper poiMapper, PoiManager poiManager, PoiAssetLoader poiAssetLoader) {
        return new OsmBackend(loginPreferences, bus, osmProxy, overpassRestClient, osmRestClient, poiMapper, poiManager, poiAssetLoader);
    }

    @Singleton
    @Provides
    RestAdapter getRestAdapter(Persister persister, OkHttpClient okHttpClient, ConfigManager configManager) {


        return new RestAdapter.Builder()
                .setEndpoint(configManager.getBasePoiApiUrl())
                .setClient(new OkClient(okHttpClient))
                .setConverter(getXMLConverterWithDateTime(persister))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog("-------------------->"))
                .build();
    }

    @Provides
    SyncWayManager getSyncWayManager(OSMProxy osmProxy, OverpassRestClient overpassRestClient, PoiMapper poiMapper, PoiManager poiManager, EventBus bus, PoiNodeRefDao poiNodeRefDao, OsmRestClient osmRestClient) {
        return new OSMSyncWayManager(osmProxy, overpassRestClient, poiMapper, poiManager, bus, poiNodeRefDao, osmRestClient);
    }

    @Provides
    SyncNoteManager getSyncNoteManager(OSMProxy osmProxy, OsmRestClient osmRestClient, EventBus bus, NoteMapper noteMapper) {
        return new OSMSyncNoteManager(osmProxy, osmRestClient, bus, noteMapper);
    }

    @Provides
    OsmRestClient getOsmService(RestAdapter restAdapter) {
        return restAdapter.create(OsmRestClient.class);
    }

    @Provides
    OverpassRestClient getOverpassRestClient(Persister persister, AuthorisationInterceptor interceptor, ConfigManager configManager) {

        return new RestAdapter.Builder()
                .setEndpoint(configManager.getBaseOverpassApiUrl())
                .setConverter(getXMLConverterWithDateTime(persister))
                .setLogLevel(RestAdapter.LogLevel.HEADERS).setLog(new AndroidLog("-------------------->"))
                .build()
                .create(OverpassRestClient.class);
    }

    private XMLMapper getXMLConverterWithDateTime(Persister persister) {
        return new XMLMapper(persister);
    }
}
