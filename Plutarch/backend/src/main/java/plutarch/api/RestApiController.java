package plutarch.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import plutarch.PlutarchApplication;
import plutarch.nlp.model.HistoryEvent;
import plutarch.nlp.model.Location;
import plutarch.nlp.model.Resource;
import plutarch.nlp.model.Topic;
import plutarch.nlp.service.DataService;
import plutarch.security.model.User;
import plutarch.security.service.GenericService;


@RestController
@RequestMapping("/api")
public class RestApiController {

    @Autowired
    private GenericService userService;

    @Autowired
    private DataService dataService;

    private static final Logger logger = LoggerFactory.getLogger(PlutarchApplication.class);

    // -------------------Create A New Resource-------------------------------------------

	@RequestMapping(value = "/resource", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('CURATOR')")
    public ResponseEntity<?> createResource(@RequestBody Resource resource, UriComponentsBuilder ucBuilder) {
        logger.info("Resource added: " + resource.getUrl());

        if (dataService.CheckResourceExists(resource.getUrl())) {
            logger.info("Unable to create. A Resource with URL " + resource.getUrl() + " already exist");
            return new ResponseEntity<String>("Resource already exists", HttpStatus.OK);
        }
        dataService.AddResource(resource);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.TEXT_PLAIN);

        return new ResponseEntity<Resource>(resource, responseHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(value ="/resources/unprocessedcount", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('CURATOR')")
    public Integer getUnprocessedResourceCount(){
        return dataService.GetUnprocessedResourceCount();
    }

    @RequestMapping(value ="/users", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ADMIN_USER')")
    public List<User> getUsers(){
        logger.info("getUsers");
        return userService.findAllUsers();
    }

    @RequestMapping(value ="/event/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('CURATOR')")
    public HistoryEvent getUnknownEvent(@PathVariable("id")Long id){
        logger.info("id: " + id);
        return dataService.GetHistoryEvent(id);
    }

    @RequestMapping(value ="/events/search", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('CURATOR')")
    public List<HistoryEvent> search(@RequestBody String searchCriteria, UriComponentsBuilder ucBuilder){
	    logger.info(searchCriteria);
        return dataService.SearchHistoryEvents(searchCriteria);
    }

    @RequestMapping(value ="/events/unknown", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('CURATOR')")
    public HistoryEvent getUnknownEvent(){
        return dataService.GetNextUnknownEvent();
    }

    @RequestMapping(value ="/events/unknowncount", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('CURATOR')")
    public Integer getUnknownEventCount(){
        return dataService.GetUnknownEventsCount();
    }

    @RequestMapping(value ="/event/validity", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('CURATOR')")
    public ResponseEntity<?> updateValidity(@RequestBody HistoryEvent hEvent, UriComponentsBuilder ucBuilder){
        dataService.UpdateEventValidity(hEvent);

        return new ResponseEntity<String>("History Event Validity Updated", HttpStatus.OK);
    }

    @RequestMapping(value ="/topic/location", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('CURATOR')")
    public Topic getNextEmptyTopicLocation(){
        Topic topic = dataService.GetNextEmptyTopicLocation();

        if (topic != null)
            topic.setLocation(new Location());

        return topic;
    }

    @RequestMapping(value ="/topic/location", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('CURATOR')")
    public ResponseEntity<?> updateTopicLocation(@RequestBody Topic topic, UriComponentsBuilder ucBuilder){
        dataService.UpdateTopicLocation(topic);

        return new ResponseEntity<String>("Topic Location Updated", HttpStatus.OK);
    }

    @RequestMapping(value ="/topics/emptylocationcount", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('CURATOR')")
    public Integer getEmptyLocationCount(){
        return dataService.GetEmptyLocationCount();
    }



    @RequestMapping(value ="/topics/noresource", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('CURATOR')")
    public List<Topic> getTopicsWithNoResource(){
        List<Topic> topics = dataService.GetTopicsWithNoResource();
        return topics;
    }
}
