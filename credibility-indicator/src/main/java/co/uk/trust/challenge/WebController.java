package co.uk.trust.challenge;

import co.uk.trust.challenge.model.CredibilityResult;
import co.uk.trust.challenge.processing.Processor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by huber on 29.11.16.
 */
@Controller
public class WebController {
    private Processor processor;

    public WebController() {
        processor = new Processor();
    }

    @ModelAttribute
    public void setVaryResponseHeader(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
    }

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/result")
    public String results(@RequestParam String url, Model model, HttpServletRequest request) {
        CredibilityResult result = processor.determineCredibility(url);

        model.addAttribute("url", url);
        model.addAttribute("credibilityResult", result);

        //return "GET".equalsIgnoreCase(request.getMethod()) ? "index" : "result";
        return "index";
    }

    @RequestMapping("/credibility")
    @ResponseBody
    public CredibilityResult credibility(@RequestParam String url) {
        CredibilityResult result = processor.determineCredibility(url);

        return result;
    }
}
