package com.viglet.turing.ext.templating.client.javabean;

import com.vignette.as.client.api.beangen.ContentBeanMethod;
import com.vignette.as.client.exception.ApplicationException;
import com.vignette.as.client.exception.AuthorizationException;
import com.vignette.as.client.exception.ValidationException;
import com.vignette.ext.templating.TemplatingConstants;
import com.vignette.ext.templating.cache.RenderedManagedObjectCacheKey;
import com.vignette.ext.templating.util.RequestContext;
import com.vignette.logging.LoggingManager;
import com.vignette.logging.context.ContextLogger;
import com.viglet.turing.client.sn.HttpTurSNServer;
import com.viglet.turing.client.sn.TurSNDocument;
import com.viglet.turing.client.sn.TurSNDocumentList;
import com.viglet.turing.client.sn.TurSNQuery;
import com.viglet.turing.client.sn.TurSNQueryParamMap;
import com.viglet.turing.client.sn.facet.TurSNFacetField;
import com.viglet.turing.client.sn.facet.TurSNFacetFieldList;
import com.viglet.turing.client.sn.pagination.TurSNPagination;
import com.viglet.turing.client.sn.pagination.TurSNPaginationItem;
import com.viglet.turing.client.sn.response.QueryTurSNResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class TuringSearchResultsComponent extends TuringSearchComponent {
	private static final long serialVersionUID = 1L;
	private Integer resultsPerPage = TemplatingConstants.DEFAULT_RESULTS_PER_PAGE;
	private static ContextLogger logger = LoggingManager.getContextLogger(TuringSearchResultsComponent.class);
	private TurSNPagination turSNPagination;
	private TurSNFacetFieldList turSNFacetFieldList;
	private String keywordAttrName = null;
	private String pageAttrName = null;
	private String facetAttrName = null;

	@Override
	public long getTTL() throws ApplicationException {
		return super.getTTL();
	}

	@ContentBeanMethod
	public String getKeywordAttrName() {
		if (null == keywordAttrName) {
			try {
				keywordAttrName = (String) getAttributeValue(ATTRIBUTE_KEYWORD);
				if (null == keywordAttrName)
					keywordAttrName = DEFAULT_ATTRIBUTE_KEYWORD;
			} catch (ApplicationException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return keywordAttrName;
	}

	@ContentBeanMethod
	public String getPageAttrName() {
		if (null == pageAttrName) {
			try {
				pageAttrName = (String) getAttributeValue(ATTRIBUTE_PAGE);
				if (null == pageAttrName)
					pageAttrName = DEFAULT_ATTRIBUTE_PAGE;
			} catch (ApplicationException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return pageAttrName;
	}

	@ContentBeanMethod
	public String getFacetAttrName() {
		if (null == facetAttrName) {
			try {
				facetAttrName = (String) getAttributeValue(ATTRIBUTE_FACET);
				if (null == facetAttrName)
					facetAttrName = DEFAULT_ATTRIBUTE_FACET;
			} catch (ApplicationException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return facetAttrName;
	}

	@ContentBeanMethod
	public Integer getResultsPerPage() throws ApplicationException {
		if (getAttributeValue(ATTRIBUTE_RESULTS_PER_PAGE) != null) {
			this.resultsPerPage = ((Integer) getAttributeValue(ATTRIBUTE_RESULTS_PER_PAGE));
		}
		return this.resultsPerPage;
	}

	private String getPage(RequestContext rc) {
		return (null == rc.getParameter(getPageAttrName())) ? "1" : rc.getParameter(getPageAttrName());
	}

	private String getKeyword(RequestContext rc) {
		return (null == rc.getParameter(getKeywordAttrName())) ? "*" : rc.getParameter(getKeywordAttrName());
	}

	private String getFacet(RequestContext rc) {
		return (null == rc.getParameter(getFacetAttrName())) ? "" : rc.getParameter(getFacetAttrName());
	}

	public TurSNPagination getTurSNPagination() {
		return turSNPagination;
	}

	public void setTurSNPagination(TurSNPagination turSNPagination) {
		this.turSNPagination = turSNPagination;
	}

	public TurSNFacetFieldList getTurSNFacetFieldList() {
		return turSNFacetFieldList;
	}

	public void setTurSNFacetFieldList(TurSNFacetFieldList turSNFacetFieldList) {
		this.turSNFacetFieldList = turSNFacetFieldList;
	}

	private String getEndPoint() throws ApplicationException {
		return (String) getAttributeValue(ATTRIBUTE_ENDPOINT);
	}

	public List<TurSNPaginationItem> getAllPages() {
		return turSNPagination.getAllPages();
	}

	public List<TurSNFacetField> getFacetFields() {
		return turSNFacetFieldList.getFields();
	}

	public TurSNFacetField getAppliedFacetFields() {
		return turSNFacetFieldList.getFacetWithRemovedValues().map(turSNFacetField -> {
			return turSNFacetField;
		}).orElse(null);

	}

	private List<String> getFieldQueries(RequestContext rc) {
		ArrayList<String> retorno = new ArrayList<>();
		String facetParams = getFacet(rc);
		if (logger.isDebugEnabled())
			logger.debug("TuringSearchResultsComponent getFieldQueries " + facetParams);
		getFacetCollection(facetParams).forEach(retorno::add);
		return retorno;
	}

	public List<String> getFacetCollection(String str) {
		return Collections.list(new StringTokenizer(str, "!")).stream().map(String.class::cast)
				.collect(Collectors.toList());
	}

	public String getFacetQueryStringParam(RequestContext rc) {
		String retorno = "";
		if (null != getFacet(rc) && !getFacet(rc).equals(""))
			retorno = getFacetAttrName() + "=" + getFacet(rc);
		return retorno;
	}

	@ContentBeanMethod
	public List<TurSNDocument> getResults(RequestContext rc)
			throws ApplicationException, ValidationException, AuthorizationException {
		int page = Integer.parseInt(getPage(rc));
		getFieldQueries(rc);
		HttpTurSNServer turSNServer = new HttpTurSNServer(getEndPoint());
		TurSNQuery query = new TurSNQuery();
		query.setQuery(getKeyword(rc));
		query.setRows(getResultsPerPage());
		query.setSortField(TurSNQuery.ORDER.asc);
		query.setPageNumber(page);
		query.setFieldQueries(getFieldQueries(rc));
		QueryTurSNResponse response = turSNServer.query(query);
		TurSNDocumentList turSNResults = response.getResults();
		setTurSNPagination(response.getPagination());
		setTurSNFacetFieldList(response.getFacetFields());
		if (logger.isDebugEnabled())
			logger.debug("TuringSearchResultsComponent - getResults " + getKeyword(rc) + " - "
					+ turSNResults.getTurSNDocuments().size());
		return turSNResults.getTurSNDocuments();
	}

	@Override
	public String createCacheKey(RenderedManagedObjectCacheKey key) throws ApplicationException {
		String cacheKey = super.createCacheKey(key);
		RequestContext rc = key.getRequestContext();
		String keyword = rc.getParameter(getKeywordAttrName());
		String page = rc.getParameter(getPageAttrName());
		String facet = rc.getParameter(getFacetAttrName());
		if (null != keyword && !keyword.equals("*"))
			cacheKey = cacheKey + "|" + keywordAttrName + "=" + keyword;
		if (null != page)
			cacheKey = cacheKey + "|" + pageAttrName + "=" + page;
		if (null != facet && !facet.equals(""))
			cacheKey = cacheKey + "|" + facetAttrName + "=" + facet;
		if (logger.isDebugEnabled())
			logger.debug("TuringSearchResultsComponent - createCacheKey " + cacheKey);
		return cacheKey;
	}

	public String getQueryString(TurSNQueryParamMap params, RequestContext rc) {
		String retorno = "?";
		retorno += getKeywordAttrName() + "=" + params.get(TUR_ATTR_QUERY).get(0);
		retorno += "&" + getPageAttrName() + "=" + params.get(TUR_ATTR_PAGE).get(0);
		if (null != params.get(TUR_ATTR_FACET))
			retorno += "&" + getFacetAttrName() + "=" + getFacetsQueryStringValue(params.get(TUR_ATTR_FACET));
		return retorno;
	}

	public String getFacetsQueryStringValue(List<String> facetsList) {
		String separator = "";
		StringBuilder retorno = new StringBuilder();
		for (String facet : facetsList) {
			retorno.append(separator + facet);
			separator = "!";
		}
		return retorno.toString();
	}
}
