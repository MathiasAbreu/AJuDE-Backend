package br.com.psoft.ajude.services.comparators;

import br.com.psoft.ajude.entities.Campanha;

import java.util.Comparator;

public class ComparatorCampanhaPorMeta implements Comparator<Campanha> {

    @Override
    public int compare(Campanha campanha01, Campanha campanha02) {

        if(campanha01.valorRestante() > campanha02.valorRestante())
            return -1;

        if(campanha01.valorRestante() < campanha02.valorRestante())
            return 1;

        return 0;
    }
}
