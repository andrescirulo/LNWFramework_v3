package net.latin.client.widget.table;

import gwt.material.design.client.data.DataSource;

/*
 * #%L
 * GwtMaterial
 * %%
 * Copyright (C) 2015 - 2016 GwtMaterialDesign
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import gwt.material.design.client.data.loader.LoadCallback;
import gwt.material.design.client.data.loader.LoadConfig;
import gwt.material.design.client.data.loader.LoadResult;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GwtListDataSource<T> implements DataSource<T> {

    private Logger logger = Logger.getLogger(GwtListDataSource.class.getName());

    private List<T> data;

    public GwtListDataSource() {
        data = new ArrayList<>();
    }

    public GwtListDataSource(List<T> data) {
        this.data = data;
    }

    @Override
    public void load(LoadConfig<T> loadConfig, LoadCallback<T> callback) {
        try {
        	int lowerLimit=loadConfig.getOffset();
        	int upperLimit=(loadConfig.getOffset() + loadConfig.getLimit());
        	if (upperLimit>data.size()){
        		upperLimit=data.size();
        	}
        	
            final List<T> subList = data.subList(lowerLimit,upperLimit);
            callback.onSuccess(new LoadResult<T>() {
                @Override
                public List<T> getData() {
                    return subList;
                }
                @Override
                public int getOffset() {
                    return loadConfig.getOffset();
                }
                @Override
                public int getTotalLength() {
                    return data.size();
                }
                @Override
                public boolean isCacheData() {
                    return cacheData();
                }
            });
        } catch (IndexOutOfBoundsException ex) {
            // Silently ignore index out of bounds exceptions
            logger.log(Level.FINE, "ListDataSource threw index out of bounds.", ex);
            callback.onFailure(ex);
        }
    }

    public void add(int startIndex, List<T> list) {
        data.addAll(startIndex, list);
    }

    public void remove(List<T> list) {
        data.removeAll(list);
    }
    public void clear() {
    	data.clear();
    }

    public boolean cacheData() {
        return true;
    }

    @Override
    public boolean useRemoteSort() {
        return false;
    }
}
