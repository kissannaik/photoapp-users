package com.kissan.photoappusers.service.client;

import com.kissan.photoappusers.shared.AlbumDTO;
import feign.FeignException;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@FeignClient(name = "albums-ws", fallbackFactory = AlbumsFallbackFactory.class)
public interface AlbumsClient {

    @GetMapping("/users/{userId}/albums")
    public List<AlbumDTO> getAlbums(@PathVariable String userId);
}

@Component
class AlbumsFallbackFactory implements FallbackFactory<AlbumsClient>{

    @Override
    public AlbumsClient create(Throwable throwable) {
        return new AlbumsFallback(throwable);
    }
}

class AlbumsFallback implements AlbumsClient{
    private final Throwable throwable;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public AlbumsFallback(Throwable throwable){
        this.throwable = throwable;
    }

    @Override
    public List<AlbumDTO> getAlbums(String userId) {
        if(throwable instanceof FeignException && ((FeignException) throwable).status() == 404)
            logger.error("404 error occured when getAlbums() was called with userId: "+userId);
        else
            logger.error("Other error occured: "+throwable.getLocalizedMessage());

        return new ArrayList<>();
    }
}
