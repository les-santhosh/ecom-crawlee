package com.santhoshle.util;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.santhoshle.models.Item;
import com.santhoshle.models.Response;

/**
 * A Util for extracting contents
 * @author santhosh
 */
public class CrawlUtil {
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * A method to extract the contents of url and return as Response object
	 * @author santhosh
	 * @param url
	 * @return Response
	 */
	public Response extract(String url) {
		return processUrl(url);
	}

	/**
	 * A method to process url and extract elements
	 * @author santhosh
	 * @param url
	 * @return Response
	 */
	public Response processUrl(String url) {
		Response response = new Response();
		Set<String> categories = Sets.newHashSet();
		List<Item> items = Lists.newArrayList();
		if (!url.isEmpty()) {
			try {
				Document document = Jsoup.connect(url).get();

				for (Element ele : document.getAllElements()) {
					// For Snapdeal
					getCategories(ele.getElementsByAttributeValueContaining("class", "categor"), categories);
					getProducts(ele.getElementsByAttributeValueContaining("class", "product"), items);
				}
				
			} catch (IOException e) {
				logger.error("Exception while extracting the content", e);
			}
			
			response.setCategories(categories);
			response.setItems(items);

		}
		return response;
	}

	/**
	 * A method to get List of categories
	 * @author santhosh
	 * @param elements
	 * @return Set<String>
	 */
	public Set<String> getCategories(Elements elements, Set<String> categories) {
		for (Element ele : elements) {
			String categoryItem = ele.getElementsByTag("ul").text();
			stripCategoryHtml(categoryItem, categories);
		}
		return categories;
	}

	/**
	 * A method to get category data
	 * @author santhosh
	 * @param element
	 * @return Set<String>
	 */
	private Set<String> stripCategoryHtml(String element, Set<String> categories) {
		for (String categoryText : element.split("[:/]")) {
			if (categoryText != null && !categoryText.trim().isEmpty()) {
				categories.add(categoryText.trim());
			}
		}
		return categories;
	}

	/**
	 * A method to get List of products
	 * @author santhosh
	 * @param elements
	 * @return List<Item>
	 */
	public List<Item> getProducts(Elements elements, List<Item> items) {
		for (Element ele : elements) {
			getProductHtml(ele.getElementsByAttributeValueContaining("class", "product-card"), items);

		}
		return items;
	}

	/**
	 * A method to get product data
	 * @author santhosh
	 * @param element
	 * @return List<Item>
	 */
	public List<Item> getProductHtml(Elements productCard, List<Item> items) {
		Item item = null;
		for (Element ele : productCard) {
			String itemName = ele.getElementsByClass("product_name").text();
			String itemPrice = ele.getElementsByClass("product_price").text();
			if(itemName != null && !itemName.trim().isEmpty()) {
				item = new Item();
				item.setName(itemName.trim());
				item.setPrice(itemPrice.trim());
				if(!items.stream().map(Item::getName).anyMatch(itemName.trim()::equals)) {
					items.add(item);
				}
			}
		}
		return items;
	}
	
}
