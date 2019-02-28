package realestate.web;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import realestate.domain.models.binding.OfferFindBindingModel;
import realestate.domain.models.binding.OfferRegisterBindingModel;
import realestate.domain.models.service.OfferFindServiceModel;
import realestate.domain.models.service.OfferRegisterServiceModel;
import realestate.service.OfferService;

@Controller
public class OfferController {

    private final OfferService offerService;
    private final ModelMapper modelMapper;

    @Autowired
    public OfferController(OfferService offerService, ModelMapper modelMapper) {
        this.offerService = offerService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/reg")
    public String register(){
        return "register.html";
    }

    @PostMapping("/reg")
    public String registerConfirm(OfferRegisterBindingModel model){

        try {
            this.offerService.registerOffer(this.modelMapper.map(model, OfferRegisterServiceModel.class));
        }catch (IllegalArgumentException e){
            e.printStackTrace();

            return "redirect:/reg";
        }

        return "redirect:/";
    }

    @GetMapping("/find")
    public String find(){
        return "find.html";
    }

    @PostMapping("/find")
    public String findPost(OfferFindBindingModel model){

        OfferFindServiceModel offerModel = this.modelMapper.map(model, OfferFindServiceModel.class);

        return this.offerService.removeOffer(offerModel)? "redirect:/": "redirect:/find";
    }

}
