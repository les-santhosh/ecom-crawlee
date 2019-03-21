package controllers;

import javax.annotation.Resource;

import com.google.inject.Inject;
import com.santhoshle.models.Response;
import com.santhoshle.util.CrawlUtil;

import play.libs.Json;
import play.mvc.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 * @author santhosh
 */
public class HomeController extends Controller {
	
	
	@Inject
	private CrawlUtil crawlUtil;
	

    /**
     * A controller method
     * @author santhosh
     * @param url
     * @return
     */
    public Result extract(String url) {
    	Response response = crawlUtil.extract(url);
    	return ok(views.html.product.render(response));
    }

}
