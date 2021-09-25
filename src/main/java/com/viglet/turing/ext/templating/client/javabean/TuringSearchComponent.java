package com.viglet.turing.ext.templating.client.javabean;

import com.vignette.as.client.exception.ApplicationException;
import com.vignette.as.client.exception.AuthorizationException;
import com.vignette.as.client.exception.ValidationException;
import com.vignette.as.server.event.AsPrePersistenceEvent;
import com.vignette.ext.templating.util.RequestContext;
import com.vignette.logging.LoggingManager;
import com.vignette.logging.context.ContextLogger;

public abstract class TuringSearchComponent
extends com.vignette.ext.templating.client.javabean.ContentComponent
{

	public static String ATTRIBUTE_MAX_RESULTS = "ResultsPerPage";
	public static String ATTRIBUTE_RESULTS_PER_PAGE = "ResultsPerPage";
	public static String ATTRIBUTE_ENDPOINT = "Endpoint";
	public static String ATTRIBUTE_KEYWORD = "keywordAttr";
	public static String ATTRIBUTE_PAGE = "pageAttr";
	public static String ATTRIBUTE_FACET = "facetAttr";
	public static String DEFAULT_ATTRIBUTE_KEYWORD = "text";
	public static String DEFAULT_ATTRIBUTE_PAGE = "page";
	public static String DEFAULT_ATTRIBUTE_FACET = "facet";
	public static String TUR_ATTR_QUERY = "q";
	public static String TUR_ATTR_PAGE = "p";
	public static String TUR_ATTR_FACET = "fq[]";
	public static String ATTRIBUTE_CHANNEL_PATH = "ChannelPath";
	public static String ATTRIBUTE_TITLE = "vgnExtTemplatingComponentTitle";
	public static String ATTRIBUTE_HEADER = "vgnExtTemplatingComponentHeader";
	public static String ATTRIBUTE_FOOTER = "vgnExtTemplatingComponentFooter";

	private static ContextLogger LOG = LoggingManager.getContextLogger(TuringSearchComponent.class);

	public void handlePreCreate(AsPrePersistenceEvent event)
			throws ApplicationException, AuthorizationException, ValidationException
	{
		if (LOG.isTraceEnabled()) {
			LOG.trace("Entered method handlePreCreate() with event : " + event);
		}
		super.handlePreCreate(event);
		if (LOG.isTraceEnabled()) {
			LOG.trace("Exit method handlePreCreate() with event : " + event);
		}
	}

	public void handlePreUpdate(AsPrePersistenceEvent event)
			throws ApplicationException, AuthorizationException, ValidationException
	{
		if (LOG.isTraceEnabled()) {
			LOG.trace("Entered method handlePreUpdate() with event : " + event);
		}
		super.handlePreUpdate(event);
		if (LOG.isTraceEnabled()) {
			LOG.trace("Exit method handlePreUpdate() with event : " + event);
		}
	}

	public String toXML(RequestContext requestContext)
			throws ApplicationException
	{
		if (LOG.isTraceEnabled()) {
			LOG.trace("Entered method toXML() with requestContext : " + requestContext);
		}
		String xml = "";
		if (LOG.isTraceEnabled()) {
			LOG.trace("Exit method toXML() with xml : " + xml);
		}
		return xml;
	}

	public long getComponentDefaultTTL()
	{
		if (LOG.isTraceEnabled()) {
			LOG.trace("Exit method getComponentDefaultTTL() ");
		}
		long ttl = super.getComponentDefaultTTL();
		if (LOG.isTraceEnabled()) {
			LOG.trace("Exit method getComponentDefaultTTL() with ttl : " + ttl);
		}
		return ttl;
	}

	public String getAttributeValue() {
		return null;
	}
}
