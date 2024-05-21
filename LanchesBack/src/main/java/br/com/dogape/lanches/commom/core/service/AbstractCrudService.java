package br.com.dogape.lanches.commom.core.service;

import br.com.dogape.lanches.commom.util.PagePagination;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Service
public abstract class AbstractCrudService<T> {

    protected JpaRepository<T, Long> repository;

    public Optional<T> findById(Long id) {
        return repository.findById(id);
    }

    public List<T> findAll() {
        return repository.findAll();
    }

    public Page<T> findAll(T filter, Pageable pageable){
        Page<T> page = repository.findAll(createExample(filter), pageable);

        return new PagePagination<>(page.getContent(), page.getPageable(), page.getTotalElements());
    }

    private Example<T> createExample(T filter){
        ExampleMatcher caseInsensitiveExampleMatcher = ExampleMatcher.matchingAll().withIgnoreCase();
        return Example.of(filter, caseInsensitiveExampleMatcher);
    }

    public T save(T data) {
        return repository.save(data);
    }

    public T insert(T data) {
        return repository.save(data);
    }

    public T update(T data) {
        return repository.save(data);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void delete(T data) {
        repository.delete(data);
    }

    @Transactional(rollbackFor = Throwable.class)
    public T deleteById(Long id) {
        T data = repository.getReferenceById(id);
        repository.deleteById(id);
        return data;
    }
}
