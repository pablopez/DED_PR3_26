package uoc.ds.pr.repository;

import uoc.ds.pr.exceptions.RatingNotFoundException;
import uoc.ds.pr.model.Assistance;
import uoc.ds.pr.model.Rating;

import java.time.LocalDate;

import static uoc.ds.pr.pr3.SystemIssuesPR3.MAX_RATING;

public class RatingRepository extends AbstractRepository<Rating> {

    public RatingRepository() {
        super(new VectorStorageStrategy<>(MAX_RATING));
    }

    public Rating addRating(
            String id,
            Assistance assistance,
            LocalDate date,
            int resolutionScore,
            int speedScore,
            int treatmentScore
    ) {
        Rating rating = getById(id);

        if (rating == null) {
            rating = new Rating(
                    id,
                    assistance,
                    date,
                    resolutionScore,
                    speedScore,
                    treatmentScore
            );

            save(rating);
        } else {
           rating.update(assistance,
                   date,
                   resolutionScore,
                   speedScore,
                   treatmentScore
           );
        }

        return rating;
    }

    public Rating getRating(String id) {
        return getById(id);
    }

    public Rating getRatingOrThrow(String id)
            throws RatingNotFoundException {
        return getByIdOrThrow(id, RatingNotFoundException::new);
    }

    public int numRatings() {
        return size();
    }
}