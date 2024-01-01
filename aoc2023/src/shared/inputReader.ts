import { readFileSync } from 'fs';

/**
 * This function assumes that input files lie in the same directory as
 * the script that executes it.
 *
 * @param day_id 2-digit day identifier (e.g. 01, 14).
 * @param example If set to `true`, example.txt file will be read. Otherwise, input.txt will be read.
 */
export function readInput(day_id: string, example = false): string[] {
	// path relative to the project root
	const base_path = `./src/puzzles/day${day_id}`;
	const path = example ? `${base_path}/example.txt` : `${base_path}/input.txt`;
	const fileContents = readFileSync(path);
	const lines = fileContents.toString().split('\n');
	// remove empty line at the end if present
	if (lines[lines.length - 1] == '') {
		lines.pop();
	}
	return lines;
}
