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

package org.kie.workbench.common.stunner.bpmn.client.shape.def;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import org.kie.workbench.common.stunner.bpmn.client.resources.BPMNSVGGlyphFactory;
import org.kie.workbench.common.stunner.bpmn.client.resources.BPMNSVGViewFactory;
import org.kie.workbench.common.stunner.bpmn.client.shape.view.handler.EventCancelActivityViewHandler;
import org.kie.workbench.common.stunner.bpmn.definition.BaseCatchingIntermediateEvent;
import org.kie.workbench.common.stunner.bpmn.definition.IntermediateErrorEventCatching;
import org.kie.workbench.common.stunner.bpmn.definition.IntermediateSignalEventCatching;
import org.kie.workbench.common.stunner.bpmn.definition.IntermediateTimerEvent;
import org.kie.workbench.common.stunner.core.client.shape.SvgDataUriGlyph;
import org.kie.workbench.common.stunner.core.client.shape.view.HasTitle;
import org.kie.workbench.common.stunner.core.client.shape.view.handler.CompositeShapeViewHandler;
import org.kie.workbench.common.stunner.core.client.shape.view.handler.FontHandler;
import org.kie.workbench.common.stunner.core.client.shape.view.handler.SizeHandler;
import org.kie.workbench.common.stunner.core.definition.shape.Glyph;
import org.kie.workbench.common.stunner.svg.client.shape.factory.SVGShapeViewResources;
import org.kie.workbench.common.stunner.svg.client.shape.view.SVGShapeView;

public class CatchingIntermediateEventShapeDef
        implements BPMNSvgShapeDef<BaseCatchingIntermediateEvent> {

    public static final SVGShapeViewResources<BaseCatchingIntermediateEvent, BPMNSVGViewFactory> VIEW_RESOURCES =
            new SVGShapeViewResources<BaseCatchingIntermediateEvent, BPMNSVGViewFactory>()
                    .put(IntermediateTimerEvent.class,
                         BPMNSVGViewFactory::intermediateTimerEvent)
                    .put(IntermediateSignalEventCatching.class,
                         BPMNSVGViewFactory::intermediateSignalCatchingEvent)
                    .put(IntermediateErrorEventCatching.class,
                         BPMNSVGViewFactory::intermediateErrorCatchingEvent);

    public static final Map<Class<? extends BaseCatchingIntermediateEvent>, SvgDataUriGlyph> GLYPHS =
            new HashMap<Class<? extends BaseCatchingIntermediateEvent>, SvgDataUriGlyph>() {{
                put(IntermediateTimerEvent.class,
                    BPMNSVGGlyphFactory.INTERMEDIATE_TIMER_EVENT_GLYPH);
                put(IntermediateSignalEventCatching.class,
                    BPMNSVGGlyphFactory.INTERMEDIATE_SIGNAL_EVENT_GLYPH);
                put(IntermediateErrorEventCatching.class,
                    BPMNSVGGlyphFactory.INTERMEDIATE_ERROR_EVENT_GLYPH);
            }};

    @Override
    public FontHandler<BaseCatchingIntermediateEvent, SVGShapeView> newFontHandler() {
        return newFontHandlerBuilder()
                .positon(event -> HasTitle.Position.BOTTOM)
                .build();
    }

    @Override
    public SizeHandler<BaseCatchingIntermediateEvent, SVGShapeView> newSizeHandler() {
        return newSizeHandlerBuilder()
                .radius(task -> task.getDimensionsSet().getRadius().getValue())
                .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public BiConsumer<BaseCatchingIntermediateEvent, SVGShapeView> viewHandler() {
        return new CompositeShapeViewHandler<BaseCatchingIntermediateEvent, SVGShapeView>()
                .register(newViewAttributesHandler())
                .register(new EventCancelActivityViewHandler())::handle;
    }

    @Override
    public SVGShapeView<?> newViewInstance(final BPMNSVGViewFactory factory,
                                           final BaseCatchingIntermediateEvent task) {
        return VIEW_RESOURCES
                .getResource(factory,
                             task)
                .build(false);
    }

    @Override
    public Glyph getGlyph(final Class<? extends BaseCatchingIntermediateEvent> type) {
        return GLYPHS.get(type);
    }
}
