package io.renzen.ink.Services;

import io.renzen.ink.DomainObjects.RenderShape;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

//@Service
//public class RenderObjectService {
//
//    final RenderObjectRepository renderObjectRepository;
//
//    public RenderObjectService(RenderObjectRepository renderObjectRepository) {
//        this.renderObjectRepository = renderObjectRepository;
//    }
//
//    public void addRenderShape(Shape shape) {
//        renderObjectRepository.save(shape);
//    }
//
//}


@Service
@Getter
public class RenderObjectService {

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

//        for (RenderShape renderShape : renderShapeArrayList){
//            if (renderShape.getId().equals(name)){
//                renderShapeArrayList.remove(renderShape);
//            }
//        }

    }

}