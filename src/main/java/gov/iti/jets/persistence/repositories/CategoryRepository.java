package gov.iti.jets.persistence.repositories;

import gov.iti.jets.domain.models.Category;
import gov.iti.jets.domain.models.Product;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class CategoryRepository extends AbstractRepository{

    public CategoryRepository( EntityManager entityManager ) {
        super( entityManager );
        this.setClazz( Category.class );
    }

    public static Optional<List<Product>> getProductListOfCategory( EntityManager em, int categoryId){

        return Optional.ofNullable(em.createQuery( "select c.products from Category c where c.id = :id" ).setParameter( "id", categoryId ).getResultList());
    }


}
