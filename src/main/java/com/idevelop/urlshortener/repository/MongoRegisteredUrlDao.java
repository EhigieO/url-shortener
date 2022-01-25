package com.idevelop.urlshortener.repository;

import com.idevelop.urlshortener.UnknownShortenedUrlException;
import com.idevelop.urlshortener.dao.RegisteredUrlDao;
import com.idevelop.urlshortener.model.RegisteredUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.net.URL;
@Repository
public class MongoRegisteredUrlDao implements RegisteredUrlDao {
    @Autowired
    private MongoOperations mongoOps;

    @Override
    public RegisteredUrl getRegisteredUrlById(String id) throws UnknownShortenedUrlException {
        Query query = new Query(Criteria.where("id").is(id));
        RegisteredUrl result = this.mongoOps.findOne(query, RegisteredUrl.class);
        if (result == null){
            throw new UnknownShortenedUrlException(id);
        }
        return result;
    }

    @Override
    public void createRegisteredUrl(RegisteredUrl registeredUrl) {
        mongoOps.save(registeredUrl);
    }

    @Override
    public RegisteredUrl findRegisteredUrl(URL url) throws UnknownShortenedUrlException {
        Query query = new Query(Criteria.where("url").is(url.toString()));
        RegisteredUrl result = this.mongoOps.findOne(query, RegisteredUrl.class);
        if (result == null){
            throw new UnknownShortenedUrlException(url);
        }
        return result;
    }
}
