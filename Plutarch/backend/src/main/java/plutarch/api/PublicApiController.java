package plutarch.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import plutarch.PlutarchApplication;
import plutarch.nlp.model.HistoryEvent;
import plutarch.nlp.model.Location;
import plutarch.nlp.model.Resource;
import plutarch.nlp.model.Topic;
import plutarch.nlp.service.DataService;
import plutarch.security.model.User;
import plutarch.security.service.GenericService;

import java.util.List;


@RestController
@RequestMapping("/public/api")
public class PublicApiController {

    @Autowired
    private GenericService userService;

    @Autowired
    private DataService dataService;

    private static final Logger logger = LoggerFactory.getLogger(PlutarchApplication.class);

    @RequestMapping(value ="/events/map", method = RequestMethod.GET)
    public List<HistoryEvent> getHistoryEventsMap(){
        return dataService.GetHistoryEventsMap();
    }
}
