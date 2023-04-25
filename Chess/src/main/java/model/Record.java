package model;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Record implements Iterable<Move> {
	private ArrayList<Move> list;
	private ArrayList<String> idList;
	private EndGame endgame;
	private boolean firebaseInitialized;
	private FirebaseDatabase database;
	private DatabaseReference recordRef;

	public Record() {
		list = new ArrayList<>();
		idList = new ArrayList<>();
		endgame = null;
		firebaseInitialized = false;
	}

	public int size() {
		return list.size();
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	public boolean contains(Object o) {
		return list.contains(o);
	}

	public void add(Move move) {
		list.add(move);
		// Initialize Firebase if not already initialized
		if (!firebaseInitialized) {
			initFirebase();
			firebaseInitialized = true;
		}
		// Store the new move in Firebase
		DatabaseReference newMoveRef = recordRef.push();
		String moveId = newMoveRef.getKey();
		idList.add(moveId);
		HashMap<String, Object> moveMap = new HashMap<String, Object>();
		moveMap.put("isWhite", move.getWhoseTurn());
		moveMap.put("movedPiece", move.movedPiece.toString()); // Assuming movedPiece is a serializable object
		moveMap.put("startPosition", move.startPosition.toString()); // Assuming startPosition is a serializable object
		moveMap.put("capturedPiece", move.capturedPiece == null ? null : move.capturedPiece.toString()); // Assuming capturedPiece is a serializable object
		moveMap.put("lastPosition", move.lastPosition.toString()); // Assuming lastPosition is a serializable object
		moveMap.put("note", move.note.toString()); // Assuming note is an enum
		newMoveRef.setValue(moveMap, new DatabaseReference.CompletionListener() {
			@Override
			public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
				if (databaseError != null) {
					// Handle the error
				}
			}
		});
	}

	public void removeLast() {
		if (list.isEmpty()) {
			return;
		}
		// Remove the last move from Firebase
		if (firebaseInitialized) {
			String lastMoveId = idList.get(idList.size() - 1);
			DatabaseReference lastMoveRef = recordRef.child(lastMoveId);
			lastMoveRef.removeValue(new DatabaseReference.CompletionListener() {
				@Override
				public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
					if (databaseError != null) {
						// Handle the error
					}
				}
			});
			idList.remove(idList.size() - 1);
		}
		// Remove the last move from the list in memory
		list.remove(list.size() - 1);
	}

	public Move get(int time) {
		return list.get(time);
	}

	@Override
	public Iterator<Move> iterator() {
		return list.iterator();
	}

	public Move getLastMove() {
		if (isEmpty()) {
			return null;
		}
		return get(size() - 1);
	}

	public boolean hasEnd() {
		return endgame != null;
	}

	public void endGame(EndGame endgame) {
		this.endgame = endgame;
	}

	public boolean hasMoved(Square original, Class<? extends Piece> type, int time) {
		if (!original.isOccupied() || !original.getPiece().isType(type)) {
			return true;
		}
		for (int t = 0; t < time; t++) {
			if (original.equals(get(t).getStart())) {
				return true;
			}
		}
		return false;
	}

	public String printDoc() {
		StringBuilder sb = new StringBuilder();
		int round = 1;
		for (Move r : list) {
			if (r.isWhite) {
				sb.append(round + ". " + r.getDoc());
			} else {
				round++;
				sb.append("   " + r.getDoc() + "\n");
			}
		}
		if (hasEnd()) {
			sb.append("\n" + endgame.getDoc());
		} else {
			Move last = getLastMove();
			if (last != null) {
				if (last.isWhite) {
					sb.append("   ...");
				}
			}
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return printDoc();
	}

	public String getEndGameDescript() {
		return endgame.getDescript();
	}

	public EndGame getEndGame() {
		return endgame;
	}

	private void initFirebase() {
		FileInputStream serviceAccount = null;
		try {
			serviceAccount = new FileInputStream("C:\\Users\\srina_1pv\\StudioProjects\\Chess\\secret\\chess-82ec0-firebase-adminsdk-ob4tb-e44d326a56.json");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		FirebaseOptions options = null;
		try {
			options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setDatabaseUrl("https://chess-82ec0-default-rtdb.firebaseio.com/")
					.build();
		} catch (IOException e) {
			e.printStackTrace();
		}

		FirebaseApp.initializeApp(options);
		database = FirebaseDatabase.getInstance();
		recordRef = database.getReference().push();
	}
}