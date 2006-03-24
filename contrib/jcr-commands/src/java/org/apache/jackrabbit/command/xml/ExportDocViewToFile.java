/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  The ASF licenses this file to You
 * under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.jackrabbit.command.xml;

import java.io.IOException;
import java.io.OutputStream;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;

/**
 * Serialize the <code>Node</code> to the given file using the Document View
 * Format
 */
public class ExportDocViewToFile extends AbstractExportViewToFile {

    /**
     * {@inheritDoc}
     */
    protected void exportView(
        Node node,
        OutputStream out,
        boolean skipBinary,
        boolean noRecurse) throws PathNotFoundException, IOException,
            RepositoryException {
        node.getSession().exportDocumentView(node.getPath(), out, skipBinary,
            noRecurse);
    }
}
