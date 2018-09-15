package com.clientui.exceptions;

import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new ErrorDecoder.Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 400) {
            return new ProductBadRequestException("Requète incorrecte");
        } else if (response.status() == 404) {
            return new ProductNotFoundException("Produit non trouvé");
        }

        return defaultErrorDecoder.decode(methodKey, response);
    }
}
