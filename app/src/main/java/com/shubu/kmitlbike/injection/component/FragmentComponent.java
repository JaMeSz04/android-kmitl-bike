package com.shubu.kmitlbike.injection.component;

import com.shubu.kmitlbike.injection.PerFragment;
import com.shubu.kmitlbike.injection.module.FragmentModule;

import dagger.Subcomponent;

/**
 * This component inject dependencies to all Fragments across the application
 */
@PerFragment
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {

}