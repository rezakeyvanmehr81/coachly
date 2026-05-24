package org.example.coachly.service;

import org.example.coachly.model.Item;
import org.example.coachly.model.User;
import org.example.coachly.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item createItem(String name, String description, String category,
                           Double pricePerDay, Double securityDeposit,
                           String location, String imageUrl, User owner) {
        Item item = new Item();
        item.setName(name);
        item.setDescription(description);
        item.setCategory(category);
        item.setPricePerDay(pricePerDay);
        item.setSecurityDeposit(securityDeposit);
        item.setLocation(location);
        item.setImageUrl(imageUrl);
        item.setOwner(owner);

        return itemRepository.save(item);
    }

    public List<Item> getAllItems() {
        return itemRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<Item> getItemById(Long id) {
        return itemRepository.findById(id);
    }

    public List<Item> getItemsByOwner(User owner) {
        return itemRepository.findByOwner(owner);
    }

    public List<Item> searchItems(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllItems();
        }
        return itemRepository
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrCategoryContainingIgnoreCase(
                        query, query, query);
    }

    public List<Item> searchWithFilters(String query, String category, Double maxPrice) {
        return itemRepository.findWithFilters(
                (query != null && query.trim().isEmpty()) ? null : query,
                (category != null && category.trim().isEmpty()) ? null : category,
                maxPrice);
    }

    /**
     * Delete an item only if the requesting user is the owner.
     *
     * @return true if deleted, false if not authorized
     */
    public boolean deleteItem(Long itemId, User user) {
        Optional<Item> itemOpt = itemRepository.findById(itemId);
        if (itemOpt.isPresent() && itemOpt.get().getOwner().getId().equals(user.getId())) {
            itemRepository.delete(itemOpt.get());
            return true;
        }
        return false;
    }

    /**
     * Delete an item bypassing ownership check (Admin only).
     */
    public void deleteItemAdmin(Long itemId) {
        itemRepository.deleteById(itemId);
    }
}

