package io.renzen.ink.DomainObjects;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.awt.image.DataBuffer;

@NoArgsConstructor
@Data
@Document(collection = "images")
public class ImageDO {
    @MongoId
    ObjectId _id;
    String name;
    DataBuffer dataBuffer;
    //Raster imageRaster;
    //BufferedImage image;
}
