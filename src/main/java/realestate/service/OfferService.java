package realestate.service;

import realestate.domain.models.binding.OfferFindBindingModel;
import realestate.domain.models.service.OfferFindServiceModel;
import realestate.domain.models.service.OfferRegisterServiceModel;
import java.util.List;


public interface OfferService {

    void registerOffer(OfferRegisterServiceModel offerServiceModel);

    List<OfferRegisterServiceModel> findAllOffers();

    boolean removeOffer(OfferFindServiceModel model);
}
