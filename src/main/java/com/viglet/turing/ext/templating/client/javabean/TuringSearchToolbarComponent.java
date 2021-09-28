package com.viglet.turing.ext.templating.client.javabean;

import com.vignette.as.client.api.beangen.ContentBeanMethod;
import com.vignette.as.client.exception.ApplicationException;
import com.vignette.ext.templating.util.RequestContext;
import com.vignette.logging.LoggingManager;
import com.vignette.logging.context.ContextLogger;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class TuringSearchToolbarComponent
extends TuringSearchComponent
{
	private static ContextLogger logger = LoggingManager.getContextLogger(TuringSearchToolbarComponent.class);
	private static final long serialVersionUID = 1L;
	private String keywordAttrName = null;
	public Collection<String> getRequiredRequestParameterNames(RequestContext rc)
			throws ApplicationException
	{
		Collection<String> requiredRequestParameterNames = new HashSet<>(Arrays.asList(new String[] { getKeywordAttrName(), "vgnextoid" }));
		addDisplayViewRequiredRequestParameterNames(requiredRequestParameterNames);
		return requiredRequestParameterNames;
	}

	@ContentBeanMethod
	public String getKeywordAttrName()
	{
		if (null == keywordAttrName)
		{
			try {
				keywordAttrName =  (String) getAttributeValue(ATTRIBUTE_KEYWORD);
				if (null == keywordAttrName)
					keywordAttrName = DEFAULT_ATTRIBUTE_KEYWORD;
			} catch (ApplicationException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return keywordAttrName;
	}






}
