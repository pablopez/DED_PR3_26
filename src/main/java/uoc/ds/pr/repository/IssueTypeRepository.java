package uoc.ds.pr.repository;

import uoc.ds.pr.exceptions.IssueTypeNotFoundException;
import uoc.ds.pr.model.IssueType;
import uoc.ds.pr.storage.VectorStorageStrategy;

import static uoc.ds.pr.pr3.SystemIssuesPR3.MAX_ISSUE_TYPES;

public class IssueTypeRepository extends AbstractRepository<IssueType> {

    public IssueTypeRepository() {
        super(new VectorStorageStrategy<>(MAX_ISSUE_TYPES));
    }

    public IssueType addIssueType(String id, String name) {
        return addElement(
                id,
                key -> new IssueType(key, name),
                (issueType, data) -> issueType.setName(data[0]),
                name
        );
    }

    public IssueType getIssueType(String id) {
        return getById(id);
    }

    public IssueType getIssueTypeOrThrow(String id)
            throws IssueTypeNotFoundException {
        return getByIdOrThrow(id, IssueTypeNotFoundException::new);
    }

    public int numIssueTypes() {
        return size();
    }
}