package io.renzen.ink.Services;

import io.renzen.ink.ArtObjects.RenderShape;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Getter
public class RenderShapeService {

    final ArrayList<RenderShape> renderShapeArrayList = new ArrayList<>();

    public RenderShape addRenderShape(RenderShape renderShape) {
        renderShapeArrayList.add(renderShape);
        return renderShape;
    }

    public RenderShape findByName(String name) {
        for (RenderShape renderShape : renderShapeArrayList) {
            if (renderShape.getId().equals(name)) {
                return renderShape;
            }
        }
        return null;
    }

    public void deleteByName(String name) {
        renderShapeArrayList.removeIf(renderShape -> renderShape.getId().equals(name));
    }

}