package dev.chiedo.bookapi.mapper;

public interface IMapper<A, B> {

    B mapTo(A a);

    A mapFrom(B b);
}
