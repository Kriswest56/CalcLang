package main;

/*
  store identifier with integer info
*/

import java.util.ArrayList;

public class NameIntTable {

    private ArrayList<NameIntPair> table;

    public NameIntTable() {
        table = new ArrayList<NameIntPair>();
    }

    // add given name, number pair
    public void add( String s, int num ) {
        table.add( new NameIntPair( s, num ) );
    }

    public String toString() {
        String s = "----\n";
        for( int k=0; k<table.size(); k++ ) {
            NameIntPair pair = table.get(k);
            s += pair + "\n";
        }
        return s;
    }

    public int size() {
        return table.size();
    }

    public NameIntPair get( int index ) {
        return table.get( index );
    }

    public int getNumber( String target ) {
        for( NameIntPair pair : table ) {
            if( pair.name.equals(target) )
                return pair.number;
        }
        return -1;
    }

    public String getName( int index ) {
        return table.get(index).name;
    }

    public int getNumber( int index ) {
        return table.get(index).number;
    }

    public void markInUse( int index ) {
        String s = table.get(index).name;
        s = s.substring( 0, s.length()-1 ); // toss the ? on end
        table.set( index, new NameIntPair(s,index) );
    }

    // this method returns cell number of an unused aux,
    // might need to make a new one if none unused
    public int getAux() {
        // search for unused aux:
        for( int k=0; k<table.size(); k++ ) {
            NameIntPair pair = table.get(k);
            if( pair.name.endsWith("?") ) {
                markInUse( k );
                return pair.number;
            }
        }

        // didn't find unused aux, make another
        int num = 1;//Node.nextAux();
        add( "$" + num, table.size() );
        return table.size()-1;
    }

    // mark this cell as unused aux
    public void releaseAux( int aux ) {
        for( int k=0; k<table.size(); k++ ) {
            NameIntPair pair = table.get(k);
            if( pair.number == aux ) {
                table.set( k, new NameIntPair( pair.name + "?", pair.number ) );
            }
        }
    }

}