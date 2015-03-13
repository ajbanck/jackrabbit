/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jackrabbit.aws.ext.ds;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.jcr.RepositoryException;

import org.apache.jackrabbit.aws.ext.S3Constants;
import org.apache.jackrabbit.core.data.CachingDataStore;
import org.apache.jackrabbit.core.data.DataRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test S3DataStore operation with SSE_S3 encryption.
 */
public class TestS3DSWithSSES3 extends TestS3Ds {

    protected static final Logger LOG = LoggerFactory.getLogger(TestS3DSWithSSES3.class);

    public TestS3DSWithSSES3() throws IOException {
        config = System.getProperty(CONFIG);
        memoryBackend = false;
        noCache = true;
    }

    protected CachingDataStore createDataStore() throws RepositoryException {
        props.setProperty(S3Constants.S3_ENCRYPTION,
            S3Constants.S3_ENCRYPTION_SSE_S3);
        ds = new S3TestDataStore(props);
        ds.setConfig(config);
        ds.init(dataStoreDir);
        sleep(1000);
        return ds;
    }

    /**
     * Test data migration enabling SSE_S3 encryption.
     */
    public void testDataMigration() {
        try {
            String bucket = props.getProperty(S3Constants.S3_BUCKET);
            ds = new S3TestDataStore(props);
            ds.setConfig(config);
            ds.setCacheSize(0);
            ds.init(dataStoreDir);
            byte[] data = new byte[dataLength];
            randomGen.nextBytes(data);
            DataRecord rec = ds.addRecord(new ByteArrayInputStream(data));
            assertEquals(data.length, rec.getLength());
            assertRecord(data, rec);
            ds.close();

            // turn encryption now.
            props.setProperty(S3Constants.S3_BUCKET, bucket);
            props.setProperty(S3Constants.S3_ENCRYPTION,
                S3Constants.S3_ENCRYPTION_SSE_S3);
            props.setProperty(S3Constants.S3_RENAME_KEYS, "true");
            ds = new S3TestDataStore(props);
            ds.setConfig(config);
            ds.setCacheSize(0);
            ds.init(dataStoreDir);

            rec = ds.getRecord(rec.getIdentifier());
            assertEquals(data.length, rec.getLength());
            assertRecord(data, rec);

            randomGen.nextBytes(data);
            rec = ds.addRecord(new ByteArrayInputStream(data));
            ds.close();

        } catch (Exception e) {
            LOG.error("error:", e);
            fail(e.getMessage());
        }
    }

}
