/*
 * Copyright (C) 2009 Mathias Doenitz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.parboiled.matchers;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.parboiled.Rule;
import org.parboiled.trees.ImmutableGraphNode;

/**
 * Abstract base class of most regular {@link Matcher}s.
 *
 * @param <V> the type of the value field of a parse tree node
 */
public abstract class AbstractMatcher<V> extends ImmutableGraphNode<Matcher<V>> implements Rule, Matcher<V>, Cloneable {

    private String defaultLabel;
    private String label;
    private boolean locked;
    private boolean leaf;

    protected AbstractMatcher() {
        this(new Rule[0]);
    }

    @SuppressWarnings({"unchecked"})
    protected AbstractMatcher(@NotNull Rule subRule) {
        this(new Rule[] {subRule});
    }

    @SuppressWarnings({"unchecked"})
    protected AbstractMatcher(@NotNull Rule[] subRules) {
        super(ImmutableList.<Matcher<V>>of(toMatchers(subRules)));
    }

    private static Matcher[] toMatchers(@NotNull Rule[] subRules) {
        Matcher[] matchers = new Matcher[subRules.length];
        for (int i = 0; i < subRules.length; i++) {
            matchers[i] = (Matcher) subRules[i];
        }
        return matchers;
    }

    public boolean isLocked() {
        return locked;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public void lock() {
        locked = true;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public String getLabel() {
        return label != null ? label : defaultLabel;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public boolean hasCustomLabel() {
        return label != null;
    }

    public AbstractMatcher<V> defaultLabel(String defaultLabel) {
        this.defaultLabel = defaultLabel;
        return this;
    }

    @Override
    public String toString() {
        return getLabel();
    }

    @SuppressWarnings({"unchecked"})
    public AbstractMatcher<V> label(@NotNull String label) {
        if (label.equals(this.label)) return this;
        AbstractMatcher<V> matcher = isLocked() ? createClone() : this;
        matcher.label = label;
        return matcher;
    }

    @SuppressWarnings({"unchecked"})
    public Rule asLeaf() {
        if (isLeaf()) return this;
        AbstractMatcher<V> matcher = isLocked() ? createClone() : this;
        matcher.leaf = true;
        return matcher;
    }

    // creates a shallow copy

    @SuppressWarnings({"unchecked"})
    private AbstractMatcher<V> createClone() {
        try {
            return (AbstractMatcher<V>) clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException();
        }
    }

}

