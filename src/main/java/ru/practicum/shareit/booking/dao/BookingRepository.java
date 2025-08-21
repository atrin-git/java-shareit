package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerId(Long bookerId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :bookerId AND b.start > CURRENT_TIMESTAMP " +
            "AND b.status = 'APPROVED'")
    List<Booking> findByBookerFutureBookings(Long bookerId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :bookerId AND b.end < CURRENT_TIMESTAMP " +
            "AND b.status = 'APPROVED'")
    List<Booking> findByBookerPastBookings(Long bookerId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :bookerId AND b.start < CURRENT_TIMESTAMP AND b.end > CURRENT_TIMESTAMP " +
            "AND b.status = 'APPROVED'")
    List<Booking> findByBookerCurrentBookings(Long bookerId);

    List<Booking> findByBookerIdAndStatus(Long bookerId, Status status);

    List<Booking> findByItemOwnerId(Long ownerId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = :ownerId AND b.start > CURRENT_TIMESTAMP " +
            "AND b.status = 'APPROVED'")
    List<Booking> findByOwnerFutureBookings(Long ownerId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = :ownerId AND b.end < CURRENT_TIMESTAMP " +
            "AND b.status = 'APPROVED'")
    List<Booking> findByOwnerPastBookings(Long ownerId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = :ownerId AND b.start < CURRENT_TIMESTAMP AND b.end > CURRENT_TIMESTAMP " +
            "AND b.status = 'APPROVED'")
    List<Booking> findByOwnerCurrentBookings(Long ownerId);

    List<Booking> findByItemOwnerIdAndStatus(Long ownerId, Status status);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.id = :itemId AND b.item.owner.id = :ownerId AND b.start < CURRENT_TIMESTAMP " +
            "AND b.status = 'APPROVED' ORDER BY b.start DESC")
    List<Booking> findLastBookings(Long itemId, Long ownerId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.id = :itemId AND b.item.owner.id = :ownerId AND b.start > CURRENT_TIMESTAMP " +
            "AND b.status = 'APPROVED' ORDER BY b.start ASC")
    List<Booking> findNextBookings(Long itemId, Long ownerId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :bookerId AND b.item.id = :itemId AND b.end < CURRENT_TIMESTAMP " +
            "AND b.status = 'APPROVED' ORDER BY b.end DESC")
    List<Booking> findLastBookingsFotItem(Long bookerId, Long itemId);
}
