package sg.edu.nus.pafworkshop8.repositories;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

@Repository
public class PokemonRepository {

    @Autowired
    private MongoTemplate template;

public List<String> getTypeofPokemon(){
    UnwindOperation unwindType= Aggregation.unwind("type");

    GroupOperation groupByTypes=Aggregation.group("type");
    SortOperation sortByType=Aggregation.sort(Sort.by(Direction.ASC,"_id"));

    Aggregation pipeline = Aggregation.newAggregation(unwindType,groupByTypes,sortByType);

    return template.aggregate(pipeline, "pokemon", Document.class)
    .getMappedResults().stream()
    .map(d->d.getString("_id"))
    .toList();
    
}

// db.pokemon.aggregate([
//     {
//         $match: { type: 'Grass' }
//     }
//     ,
//     {
//         $project: {name: 1, img: 1, _id:0}
//     }
// ])

public List<Document> getPokemonwithType(String type){
    MatchOperation typeMatch=Aggregation.match(Criteria.where("type").regex(type,"i"));
    ProjectionOperation selectAttrs=Aggregation.project("name","img").andExclude("_id");
    Aggregation pipeline=Aggregation.newAggregation(typeMatch,selectAttrs);
    AggregationResults<Document> result=template.aggregate(pipeline,"pokemon",Document.class);

    return result.getMappedResults();
}


}
