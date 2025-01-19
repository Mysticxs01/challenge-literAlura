package com.alura.literalura.service;

public interface IDataConverter {
    <T> T obtainData(String json, Class<T> clase);
}
