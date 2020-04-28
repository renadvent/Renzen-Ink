package com.erick.model;

import java.util.LinkedList;

// allows you to grab a set of Linked List
// this way strokes can iterate over sets

public class ll<T> extends LinkedList<T> {

    boolean complete;
    /*
     * 
     * format to use:
     * 
     * ll<ll<T>> sets = this_list.divide_into_sets; for (ll<T> set : sets){//going
     * over list of sets for (element : set){//going over set if (set.complete()){
     * 
     * } } }
     * 
     */

    // ?
    boolean all_sets_complete() {
        return complete;
    }

    // returns
    ll<ll<T>> get_list_of_sets(int n) {

        int size = this.size();
        int remainder = size % n;

        if (remainder == 0) {
            complete = true;
        } else {
            complete = false;
        }

        ll<ll<T>> sets = new ll<ll<T>>();

        // get complete sets
        for (int i = n; i < size; i = i + n) {
            ll<T> temp = new ll<T>();
            for (int index = (i - n); index < (i); index++) {
                temp.add(this.get(index));
            }
            temp.complete = true;
            sets.add(temp);
        }

        // get last incomplete set if necessary
        if (remainder > 0) {
            ll<T> temp = new ll<T>();
            for (int i = size - remainder; i < size; i++) {
                temp.complete = false;
                sets.add(temp);
            }
        }

        return sets;
    }
}


/*     // used to grab points for render_strokes
    public <T> LinkedList<T> grab_set(LinkedList<T> ll, int start_index, int end_index) {

        // grabs a set of ojects
        // if not enough objects are found, returns the last

        LinkedList<T> return_list = new LinkedList<T>();
        for (int i = start_index; i <= end_index; i++) {

            if (i < ll.size()) {
                return_list.add(ll.get(i));
            } else {
                return return_list;
            }

        }

        return return_list;
    } */