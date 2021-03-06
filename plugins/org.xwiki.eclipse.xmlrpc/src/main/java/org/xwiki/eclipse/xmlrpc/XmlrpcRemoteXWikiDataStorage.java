/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *
 */
package org.xwiki.eclipse.xmlrpc;

import java.util.ArrayList;
import java.util.List;

import org.apache.xmlrpc.XmlRpcException;
import org.codehaus.swizzle.confluence.SearchResult;
import org.codehaus.swizzle.confluence.ServerInfo;
import org.codehaus.swizzle.confluence.SpaceSummary;
import org.eclipse.core.runtime.Assert;
import org.xwiki.eclipse.xmlrpc.XWikiEclipseXmlrpcException;
import org.xwiki.xmlrpc.XWikiXmlRpcClient;
import org.xwiki.xmlrpc.model.XWikiClass;
import org.xwiki.xmlrpc.model.XWikiClassSummary;
import org.xwiki.xmlrpc.model.XWikiObject;
import org.xwiki.xmlrpc.model.XWikiObjectSummary;
import org.xwiki.xmlrpc.model.XWikiPage;
import org.xwiki.xmlrpc.model.XWikiPageHistorySummary;
import org.xwiki.xmlrpc.model.XWikiPageSummary;

/**
 * This class implements a remote XWiki data storage. Basically it wraps the XWiki XMLRPC interface.
 */
public class XmlrpcRemoteXWikiDataStorage
{
    private XWikiXmlRpcClient rpc;

    private boolean disposed;

    public XmlrpcRemoteXWikiDataStorage(String endpoint, String userName, String password) throws XWikiEclipseXmlrpcException
    {
        try {
            rpc = new XWikiXmlRpcClient(endpoint);
            rpc.login(userName, password);
            disposed = false;
        } catch (Exception e) {
            throw new XWikiEclipseXmlrpcException(e);
        }
    }

    public synchronized void dispose()
    {
        Assert.isTrue(!disposed);

        try {
            rpc.logout();
        } catch (XmlRpcException e) {
            // Ignore
        }

        disposed = true;
    }

    public synchronized XWikiPage getPage(String pageId) throws XWikiEclipseXmlrpcException
    {
        Assert.isTrue(!disposed);

        try {
            XWikiPage page = rpc.getPage(pageId);

            /*
             * Adjust the page ID in order to embed the language in its id. This is necessary because the page returned
             * does not embed in its id extended information. So for example requesting a page Main.WebHome?language=fr
             * returns a page whose id is Main.WebHome and its language field is 'fr'
             */
            page.setId(pageId);

            return page;
        } catch (XmlRpcException e) {
            throw new XWikiEclipseXmlrpcException(e);
        }
    }

    public synchronized List<XWikiPageSummary> getPages(String spaceKey) throws XWikiEclipseXmlrpcException
    {
        Assert.isTrue(!disposed);

        try {
            return rpc.getPages(spaceKey);
        } catch (XmlRpcException e) {
            throw new XWikiEclipseXmlrpcException(e);
        }
    }

    public synchronized List<SpaceSummary> getSpaces() throws XWikiEclipseXmlrpcException
    {
        Assert.isTrue(!disposed);

        try {
            return rpc.getSpaces();
        } catch (XmlRpcException e) {
            throw new XWikiEclipseXmlrpcException(e);
        }
    }

    public SpaceSummary getSpaceSumary(String spaceKey) throws XWikiEclipseXmlrpcException
    {
        List<SpaceSummary> spaces = getSpaces();
        for (SpaceSummary space : spaces) {
            if (space.getKey().equals(spaceKey))
                return space;
        }

        return null;
    }

    public synchronized void removeSpace(String spaceKey) throws XWikiEclipseXmlrpcException
    {
        Assert.isTrue(!disposed);

        try {
            rpc.removeSpace(spaceKey);
        } catch (XmlRpcException e) {
            throw new XWikiEclipseXmlrpcException(e);
        }
    }

    public synchronized XWikiPage storePage(XWikiPage page) throws XWikiEclipseXmlrpcException
    {
        Assert.isTrue(!disposed);

        try {
            String originalPageId = page.getId();
            page = rpc.storePage(page);
            /*
             * Adjust the page ID in order to embed the language in its id. This is necessary because the page returned
             * does not embed in its id extended information. So for example requesting a page Main.WebHome?language=fr
             * returns a page whose id is Main.WebHome and its language field is 'fr'
             */
            page.setId(originalPageId);

            return page;
        } catch (XmlRpcException e) {
            throw new XWikiEclipseXmlrpcException(e);
        }
    }

    public synchronized ServerInfo getServerInfo() throws XWikiEclipseXmlrpcException
    {
        Assert.isTrue(!disposed);

        try {
            return rpc.getServerInfo();
        } catch (XmlRpcException e) {
            throw new XWikiEclipseXmlrpcException(e);
        }
    }

    public synchronized boolean removePage(String pageId) throws XWikiEclipseXmlrpcException
    {
        Assert.isTrue(!disposed);

        try {
            return rpc.removePage(pageId);
        } catch (XmlRpcException e) {
            throw new XWikiEclipseXmlrpcException(e);
        }

    }

    public synchronized List<XWikiObjectSummary> getObjects(String pageId) throws XWikiEclipseXmlrpcException
    {
        Assert.isTrue(!disposed);

        try {
            return rpc.getObjects(pageId);
        } catch (XmlRpcException e) {
            throw new XWikiEclipseXmlrpcException(e);
        }
    }

    public synchronized XWikiObject getObject(String pageId, String className, int objectId)
        throws XWikiEclipseXmlrpcException
    {
        Assert.isTrue(!disposed);

        try {
            return rpc.getObject(pageId, className, objectId);
        } catch (XmlRpcException e) {
            throw new XWikiEclipseXmlrpcException(e);
        }
    }

    public synchronized XWikiClass getClass(String classId) throws XWikiEclipseXmlrpcException
    {
        Assert.isTrue(!disposed);

        try {
            return rpc.getClass(classId);
        } catch (XmlRpcException e) {
            throw new XWikiEclipseXmlrpcException(e);
        }
    }

    public synchronized XWikiObject storeObject(XWikiObject object) throws XWikiEclipseXmlrpcException
    {
        Assert.isTrue(!disposed);

        try {
            return rpc.storeObject(object);
        } catch (XmlRpcException e) {
            throw new XWikiEclipseXmlrpcException(e);
        }

    }

    public synchronized void storeClass(XWikiClass xwikiClass) throws XWikiEclipseXmlrpcException
    {
        // Do nothing
    }

    public synchronized boolean exists(String pageId)
    {
        try {
            XWikiPage page = getPage(pageId);
        } catch (XWikiEclipseXmlrpcException e) {
            return false;
        }

        return true;
    }

    public synchronized boolean exists(String pageId, String className, int objectId)
    {
        try {
            XWikiObject object = getObject(pageId, className, objectId);
        } catch (XWikiEclipseXmlrpcException e) {
            return false;
        }

        return true;
    }

    public List<XWikiClassSummary> getClasses() throws XWikiEclipseXmlrpcException
    {
        Assert.isTrue(!disposed);

        try {
            return rpc.getClasses();
        } catch (XmlRpcException e) {
            throw new XWikiEclipseXmlrpcException(e);
        }
    }

    public boolean removeObject(String pageId, String className, int objectId) throws XWikiEclipseXmlrpcException
    {
        Assert.isTrue(!disposed);

        try {
            return rpc.removeObject(pageId, className, objectId);
        } catch (XmlRpcException e) {
            throw new XWikiEclipseXmlrpcException(e);
        }
    }

    public XWikiPageSummary getPageSummary(String pageId) throws XWikiEclipseXmlrpcException
    {
        Assert.isTrue(!disposed);

        String[] pageIdComponents = pageId.split("\\."); //$NON-NLS-1$
        Assert.isTrue(pageIdComponents.length == 2);

        List<XWikiPageSummary> pageSummaries = getPages(pageIdComponents[0]);

        for (XWikiPageSummary pageSummary : pageSummaries) {
            if (pageSummary.getId().equals(pageId)) {
                return pageSummary;
            }
        }

        return null;
    }

    public List<XWikiPageHistorySummary> getPageHistory(String pageId) throws XWikiEclipseXmlrpcException
    {
        Assert.isTrue(!disposed);

        try {
            return rpc.getPageHistory(pageId);
        } catch (XmlRpcException e) {
            throw new XWikiEclipseXmlrpcException(e);
        }
    }

    public List<XWikiPageSummary> getAllPageIds() throws XWikiEclipseXmlrpcException
    {
        Assert.isTrue(!disposed);

        List<XWikiPageSummary> result = new ArrayList<XWikiPageSummary>();

        try {
            List<SearchResult> searchResults = rpc.searchAllPagesIds();
            for (SearchResult searchResult : searchResults) {
                XWikiPageSummary pageSummary = new XWikiPageSummary();
                pageSummary.setId(searchResult.getId());
                pageSummary.setTitle(searchResult.getTitle());
                result.add(pageSummary);
            }
        } catch (XmlRpcException e) {
            throw new XWikiEclipseXmlrpcException(e);
        }

        return result;
    }
}
