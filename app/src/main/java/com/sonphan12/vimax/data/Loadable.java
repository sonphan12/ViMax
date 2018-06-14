package com.sonphan12.vimax.data;

import android.content.Context;

import java.util.List;

public interface Loadable<T> {
    List<T> load(Context ctx);
}
