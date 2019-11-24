package br.com.psoft.ajude.services.comparators;

import br.com.psoft.ajude.entities.Campanha;

import java.util.Comparator;

public class ComparatorCampanhaPorLikes implements Comparator<Campanha> {

    @Override
    public int compare(Campanha campanha01, Campanha campanha02) {

        if(campanha01.getQuantidadeLikes() > campanha02.getQuantidadeLikes())
            return -1;

        if(campanha01.getQuantidadeLikes() < campanha02.getQuantidadeLikes())
            return 1;

        return 0;
    }
}
