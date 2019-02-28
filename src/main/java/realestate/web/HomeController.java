package realestate.web;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import realestate.config.Constants;
import realestate.domain.models.view.OfferViewModel;
import realestate.service.OfferService;
import realestate.util.HtmlReader;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final OfferService offerService;
    private final HtmlReader htmlReader;
    private final ModelMapper modelMapper;

    @Autowired

    public HomeController(OfferService offerService, HtmlReader htmlReader, ModelMapper modelMapper) {
        this.offerService = offerService;
        this.htmlReader = htmlReader;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/")
    @ResponseBody
    public String home() throws IOException {
        return replaceHtml();
    }

    private String replaceHtml() throws IOException {

        StringBuilder offersHtml = new StringBuilder();
        String html = this.htmlReader.readFile(Constants.HTML_INDEX_FILE_PATH);

        List<OfferViewModel> offers = this.offerService.findAllOffers().stream()
                .map(o -> this.modelMapper.map(o, OfferViewModel.class))
                .collect(Collectors.toList());


        if (offers.size() == 0) {
            offersHtml.append("<div class=\"apartment\" style=\"border: red solid 2px\">\n" +
                    "\t\t<p>There aren't any offers!</p>\n" +
                    "</div>");
        } else {

            for (OfferViewModel offer : offers) {
                offersHtml.append(String.format("" +
                                "<div class=\"apartment\">\n" +
                                "<p>Rent: %.2f</p>\n" +
                                "<p>Type: %s</p>\n" +
                                "<p>Commission: %.2f</p>\n" +
                                "</div>",
                        offer.getApartmentRent(),
                        offer.getApartmentType(),
                        offer.getAgencyCommission()));
            }
        }

        return html.replace("{{offers}}", offersHtml.toString());
    }
}
