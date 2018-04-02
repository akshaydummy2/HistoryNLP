package plutarch.nlp.service;

import plutarch.nlp.model.Resource;

/**
 * Created by joshs on 1/27/2018.
 */
public interface IDataService {
    Resource GetNextResource();
    void AddResource(Resource resource);
}
