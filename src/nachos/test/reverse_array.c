/*
 * Prints an array of integers. Every other integer from 0 to (SIZE-1)*2.
 And then prints it in reverse.
 */

#include "stdio.h"

#define SIZE 66

int main(void) {
    int i;
    int array[SIZE];
    int tmp;

    //initializa
    for (i = 0; i < SIZE; i++) {
        array[i] = i * 2;
    }

    printf("Original array:\n");
    for (i = 0; i < SIZE; i++) {
        printf("%d ", array[i]);
    }

    //reverse
    for (i = 0; i < SIZE / 2; i++) {
        tmp = array[i];
        array[i] = array[SIZE - 1 - i];
        array[SIZE - 1 - i] = tmp;
    }

    printf("\nReversed array:\n");
    for (i = 0; i < SIZE; i++) {
        printf("%d ", array[i]);
    }
    printf("\nDone.\n");
    return 0;
}
