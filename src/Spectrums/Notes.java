package Spectrums;

/**
 * Created by arthur on 2/27/2017.
 */
public class Notes {

    private float[] firstValues;

    public Notes() {
        firstValues = new float[] {55, 62, 32, 37, 41, 44, 49,
                                    58, 34, 39, 46, 52};
    }

    /**
     * Returns the frequency associated with the desired note
     * @param note the note
     * @param octave octave of the note between 1 and 8
     * @return
     */
    public float getFrequency(Note note, int octave) {
        if(octave < 1) return 0;
        if(octave > 8) return 0;
        int pow = 0b00000001 << (octave-1);
        switch (note) {
            case A:
                return firstValues[0] * pow;
            case B:
                return firstValues[1] * pow;
            case C:
                return firstValues[2] * pow;
            case D:
                return firstValues[3] * pow;
            case E:
                return firstValues[4] * pow;
            case F:
                return firstValues[5] * pow;
            case G:
                return firstValues[6] * pow;
            case ASharp:
                return firstValues[7] * pow;
            case BFlat:
                return firstValues[7] * pow;
            case CSharp:
                return firstValues[8] * pow;
            case DFlat:
                return firstValues[8] * pow;
            case DSharp:
                return firstValues[9] * pow;
            case EFlat:
                return firstValues[9] * pow;
            case FSharp:
                return firstValues[10] * pow;
            case GFlat:
                return firstValues[10] * pow;
            case GSharp:
                return firstValues[11] * pow;
            default:
                return firstValues[11] * pow;
        }
    }
}
