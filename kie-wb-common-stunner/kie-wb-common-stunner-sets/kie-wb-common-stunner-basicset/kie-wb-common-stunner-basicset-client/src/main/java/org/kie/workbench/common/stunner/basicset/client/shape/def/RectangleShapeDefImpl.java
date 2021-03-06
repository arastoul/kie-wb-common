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

package org.kie.workbench.common.stunner.basicset.client.shape.def;

import org.kie.workbench.common.stunner.basicset.definition.Rectangle;
import org.kie.workbench.common.stunner.core.client.shape.view.handler.SizeHandler;
import org.kie.workbench.common.stunner.shapes.client.view.RectangleView;
import org.kie.workbench.common.stunner.shapes.def.RectangleShapeDef;

public final class RectangleShapeDefImpl
        implements BaseShapeViewDef<Rectangle, RectangleView>,
                   RectangleShapeDef<Rectangle, RectangleView> {

    @Override
    public SizeHandler<Rectangle, RectangleView> newSizeHandler() {
        return newSizeHandlerBuilder()
                .width(this::getWidth)
                .height(this::getHeight)
                .build();
    }

    @Override
    public Double getWidth(final Rectangle element) {
        return element.getWidth().getValue();
    }

    @Override
    public Double getHeight(final Rectangle element) {
        return element.getHeight().getValue();
    }

    @Override
    public double getCornerRadius(final Rectangle element) {
        return 5;
    }
}
