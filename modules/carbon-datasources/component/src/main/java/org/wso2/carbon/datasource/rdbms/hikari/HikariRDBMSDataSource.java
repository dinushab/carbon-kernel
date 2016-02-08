/*
 *  Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.wso2.carbon.datasource.rdbms.hikari;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.datasource.core.common.DataSourceException;
import org.wso2.carbon.datasource.rdbms.tomcat.utils.TomcatDataSourceUtils;

import java.util.Map;
import javax.naming.Reference;
import javax.naming.StringRefAddr;

public class HikariRDBMSDataSource {
    private static Log log = LogFactory.getLog(HikariRDBMSDataSource.class);

    private HikariDataSource dataSource;

    private Reference dataSourceFactoryReference;


    private HikariConfig config;

    public HikariRDBMSDataSource(HikariConfig config) throws DataSourceException {
        this.config = config;
    }

    public HikariDataSource getDataSource() {
        if (this.dataSource == null) {
            this.dataSource = new HikariDataSource(this.config);
        }
        return this.dataSource;
    }

    public Reference getDataSourceFactoryReference() throws DataSourceException {
        if (dataSourceFactoryReference == null) {
            dataSourceFactoryReference = new Reference("javax.sql.DataSource",
                    "com.zaxxer.hikari.HikariJNDIFactory", null);

            Map<String, String> poolConfigMap =
                    TomcatDataSourceUtils.extractPrimitiveFieldNameValuePairs(this.config);
            poolConfigMap.forEach((key, value) -> dataSourceFactoryReference.add(new StringRefAddr(key, value)));
        }
        return dataSourceFactoryReference;
    }
}