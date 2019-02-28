package realestate.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import realestate.domain.entities.Offer;
import realestate.domain.models.binding.OfferFindBindingModel;
import realestate.domain.models.service.OfferFindServiceModel;
import realestate.domain.models.service.OfferRegisterServiceModel;
import realestate.repository.OfferRepository;

import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final Validator validator;
    private final ModelMapper modelMapper;

    @Autowired
    public OfferServiceImpl(OfferRepository offerRepository, Validator validator, ModelMapper modelMapper) {
        this.offerRepository = offerRepository;
        this.validator = validator;
        this.modelMapper = modelMapper;
    }

    @Override
    public void registerOffer(OfferRegisterServiceModel offerServiceModel) {

        if (this.validator.validate(offerServiceModel).size() != 0){
            throw new IllegalArgumentException("Incorrect input data.");
        }

        this.offerRepository.saveAndFlush(this.modelMapper.map(offerServiceModel, Offer.class));
    }

    @Override
    public List<OfferRegisterServiceModel> findAllOffers() {
        return this.offerRepository.findAll().stream()
                .map(o -> this.modelMapper.map(o, OfferRegisterServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean removeOffer(OfferFindServiceModel model) {

        if (validator.validate(model).size() != 0){
            return false;
        }

        Offer offer = this.offerRepository.findAll()
                .stream()
                .filter(o -> o.getApartmentType().toLowerCase().equals(model.getFamilyApartmentType().toLowerCase()))
                .filter(o -> model.getFamilyBudget()
                        .compareTo(o.getApartmentRent().add(o.getAgencyCommission()
                        .divide(new BigDecimal("100")).multiply(o.getApartmentRent()))) >= 0)
                .findFirst()
                .orElse(null);

        if (offer != null){
            this.offerRepository.delete(offer);
            return true;
        }

        return false;
    }
}
