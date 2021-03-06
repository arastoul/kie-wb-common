/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.stunner.svg.client.shape.view;

import java.util.function.Function;

public final class SVGShapeViewResource {

    private final Function<Arguments, SVGShapeView> builder;

    public SVGShapeViewResource(final Function<Arguments, SVGShapeView> builder) {
        this.builder = builder;
    }

    public SVGShapeView build(final boolean resizable) {
        return builder.apply(new Arguments(resizable));
    }

    public SVGShapeView build(final double width,
                              final double height,
                              final boolean resizable) {
        return builder.apply(new Arguments(width, height, resizable));
    }

    public static final class Arguments {

        public Double width;
        public Double heigth;
        public boolean resizable;

        public Arguments(Double width,
                         Double heigth,
                         boolean resizable) {
            this.width = width;
            this.heigth = heigth;
            this.resizable = resizable;
        }

        public Arguments(boolean resizable) {
            this.resizable = resizable;
            this.width = null;
            this.heigth = null;
        }
    }
}
