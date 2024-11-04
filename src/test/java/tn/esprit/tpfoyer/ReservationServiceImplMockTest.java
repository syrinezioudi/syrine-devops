package tn.esprit.tpfoyer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer.entity.Reservation;
import tn.esprit.tpfoyer.repository.ReservationRepository;
import tn.esprit.tpfoyer.service.ReservationServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplMockTest {

    @Mock
    ReservationRepository reservationRepository;

    @InjectMocks
    ReservationServiceImpl reservationService;

    // Instances fictives
    Reservation reservation1 = new Reservation("1", new Date(), true, new HashSet<>());
    Reservation reservation2 = new Reservation("2", new Date(), false, new HashSet<>());
    List<Reservation> listReservations = new ArrayList<>() {{
        add(reservation1);
        add(reservation2);
    }};

    @Test
    public void testRetrieveAllReservations() {
        Mockito.when(reservationRepository.findAll()).thenReturn(listReservations);

        List<Reservation> reservations = reservationService.retrieveAllReservations();

        Assertions.assertNotNull(reservations);
        Assertions.assertEquals(2, reservations.size());
        Assertions.assertEquals("1", reservations.get(0).getIdReservation());
    }

    @Test
    public void testRetrieveReservation() {
        Mockito.when(reservationRepository.findById("1")).thenReturn(Optional.of(reservation1));

        Reservation retrievedReservation = reservationService.retrieveReservation("1");

        Assertions.assertNotNull(retrievedReservation);
        Assertions.assertEquals("1", retrievedReservation.getIdReservation());
    }

    @Test
    public void testAddReservation() {
        Mockito.when(reservationRepository.save(Mockito.any(Reservation.class))).thenReturn(reservation1);

        Reservation addedReservation = reservationService.addReservation(reservation1);

        Assertions.assertNotNull(addedReservation);
        Assertions.assertEquals("1", addedReservation.getIdReservation());
    }

    @Test
    public void testRemoveReservation() {
        Mockito.doNothing().when(reservationRepository).deleteById("1");

        reservationService.removeReservation("1");

        Mockito.verify(reservationRepository, Mockito.times(1)).deleteById("1");
    }

    @Test
    public void testModifyReservation() {
        Mockito.lenient().when(reservationRepository.findById("1")).thenReturn(Optional.of(reservation1));
        Mockito.when(reservationRepository.save(Mockito.any(Reservation.class))).thenReturn(reservation1);

        // Modification de la réservation
        reservation1.setEstValide(false);
        Reservation modifiedReservation = reservationService.modifyReservation(reservation1);

        Assertions.assertNotNull(modifiedReservation);
        Assertions.assertFalse(modifiedReservation.isEstValide());

        // Vérification de l'appel au repository
        Mockito.verify(reservationRepository, Mockito.times(1)).save(Mockito.any(Reservation.class));
    }

    @Test
    public void testTrouverResSelonDateEtStatus() {
        Date testDate = new Date();
        Mockito.when(reservationRepository.findAllByAnneeUniversitaireBeforeAndEstValide(testDate, true))
                .thenReturn(listReservations);

        List<Reservation> reservations = reservationService.trouverResSelonDateEtStatus(testDate, true);

        Assertions.assertNotNull(reservations);
        Assertions.assertEquals(2, reservations.size());
    }
}


