

/**
 * @author schaef
 *
 */
public class InfeasibleCode01 {
	public InfeasibleCode01 bases;
	// from org.openecard.bouncycastle.crypto.params.NTRUSigningPrivateKeyParameters	
	boolean foo(InfeasibleCode01 other) {
		if (bases == null) {
			if (other.bases != null) {
				return false;
			}
		}
		if (bases.hashCode() != other.bases.hashCode()) {
			return false;
		}
		// ...
		return true;
	}
}
